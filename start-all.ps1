$services = @(
  "eureka-server\eureka-server",
  "user-service\user-service",
  "transaction-service\transaction-service",
  "holding-service\holding-service",
  "krakenservice\kraken-ws-service",
  "market-data-streamer\market-data-streamer",
  "gateway-crypto\gateway-crypto"
)

foreach ($service in $services) {
    Write-Host "Starting $service..."

    $fullPath = Join-Path $PWD.Path $service

    # Launch in a new PowerShell window
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd `"$fullPath`"; .\mvnw spring-boot:run"
}
