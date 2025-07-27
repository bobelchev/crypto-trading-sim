# 1. Kill all Java processes (Spring Boot services)
$javaProcs = Get-Process java -ErrorAction SilentlyContinue
if ($javaProcs) {
    $javaProcs | ForEach-Object {
        Write-Host "Stopping Java process: PID=$($_.Id)"
        Stop-Process -Id $_.Id -Force
    }
    Write-Host "Java-based Spring Boot services stopped."
} else {
    Write-Host "No Java processes found."
}

# 2. Kill PowerShell windows launched by the start-all script
Get-CimInstance Win32_Process | Where-Object {
    $_.Name -eq 'powershell.exe' -and
    ($_.CommandLine -like '*spring-boot:run*' -or $_.CommandLine -like '*mvnw*')
} | ForEach-Object {
    Write-Host "Closing PowerShell window: PID=$($_.ProcessId)"
    Stop-Process -Id $_.ProcessId -Force
}

Write-Host "All service windows closed."