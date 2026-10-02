$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

# Find the JDK used by java, including Oracle's javapath installation.
$ErrorActionPreference = 'Continue'
$javaSettings = & java -XshowSettings:properties -version 2>&1 | Out-String
$ErrorActionPreference = 'Stop'
$match = [regex]::Match($javaSettings, 'java.home\s*=\s*(.+)')
if (-not $match.Success) { throw 'Java JDK 25 or newer is required.' }
$jdk = $match.Groups[1].Value.Trim()
foreach ($tool in @('javac', 'jar', 'jpackage')) {
    if (-not (Test-Path -LiteralPath "$jdk\bin\$tool.exe")) {
        throw "Missing JDK tool: $tool"
    }
}

$buildRoot = Join-Path $PSScriptRoot ('build\exe-' + (Get-Date -Format 'yyyyMMdd-HHmmss-fff'))
$classes = Join-Path $buildRoot 'classes'
$inputDir = Join-Path $buildRoot 'input'
$destination = Join-Path $PSScriptRoot 'dist'
if (Test-Path -LiteralPath (Join-Path $destination 'STube')) {
    throw 'dist\STube already exists. Move that folder elsewhere before rebuilding.'
}
New-Item -ItemType Directory -Path $classes, $inputDir -Force | Out-Null
$sources = @(Get-ChildItem -LiteralPath 'jp', 'gnu' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
& "$jdk\bin\javac.exe" -encoding UTF-8 -d $classes -sourcepath . -cp 'lib\*' @sources
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }

# Preserve package-relative image and sound resources.
Get-ChildItem -LiteralPath 'jp', 'gnu' -Recurse -File | Where-Object {
    $_.Extension -notin @('.java', '.class')
} | ForEach-Object {
    $relative = $_.FullName.Substring($PSScriptRoot.Length + 1)
    $target = Join-Path $classes $relative
    New-Item -ItemType Directory -Path (Split-Path $target) -Force | Out-Null
    Copy-Item -LiteralPath $_.FullName -Destination $target
}
& "$jdk\bin\jar.exe" --create --file "$inputDir\STube.jar" --main-class jp.ac.kyoto_u.kueps.STube.STube -C $classes .
if ($LASTEXITCODE -ne 0) { throw 'JAR creation failed.' }
Copy-Item -Path 'lib\*.jar' -Destination $inputDir
& "$jdk\bin\jpackage.exe" --type app-image --name STube --input $inputDir --main-jar STube.jar --main-class jp.ac.kyoto_u.kueps.STube.STube --dest $destination --java-options '--enable-native-access=ALL-UNNAMED'
if ($LASTEXITCODE -ne 0) { throw 'EXE packaging failed.' }
Write-Host "Created: $destination\STube\STube.exe"

