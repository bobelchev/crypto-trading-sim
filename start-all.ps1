$services = @(
  "eureka-server\eureka-server",
  "user-service\user-service",
  "transaction-service\transaction-service",
  "holding-service\holding-service",
  "krakenservice\kraken-ws-service",
  "market-data-streamer\market-data-streamer",
  "gateway-crypto\gateway-crypto"
)

$pidFile = "service-pids.txt"
Remove-Item $pidFile -ErrorAction SilentlyContinue

foreach ($service in $services) {
    Write-Host "Starting $service..."

    $fullPath = Join-Path $PWD.Path $service

    $proc = Start-Process powershell `
        -ArgumentList "-NoExit", "-Command", "cd `"$fullPath`"; .\mvnw spring-boot:run" `
        -PassThru

    $proc.Id | Out-File -Append $pidFile
}
