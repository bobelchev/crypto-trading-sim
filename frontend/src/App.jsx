import { useState, useEffect } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";
import UserInfo from "./components/UserInfo";
import Holdings from "./components/Holdings";
import MarketData from "./components/MarketData";
import Transactions from "./components/Transactions";

import "bootstrap/dist/css/bootstrap.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

function App() {
  const [user, setUser] = useState({ userId: "", balance: "" });

  useEffect(() => {
    fetch("http://localhost:8080/users/balance?userId=1")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setUser({
          userId: "user 1",
          balance: data,
        });
      })
      .catch((err) => {
        console.error(err.message);
      });
  }, []);
  const marketPrices = [
    { symbol: "BTC/USD", price: 103234.48 },
    { symbol: "ETH/USD", price: 2333.61 },
    { symbol: "USDT/USD", price: 1.0 },
    { symbol: "XRP/USD", price: 2.35 },
    { symbol: "BNB/USD", price: 638.14 },
    { symbol: "SOL/USD", price: 172.23 },
    { symbol: "USDC/USD", price: 1.0 },
    { symbol: "DOGE/USD", price: 0.2047 },
    { symbol: "ADA/USD", price: 0.7807 },
    { symbol: "TRX/USD", price: 0.2611 },
    { symbol: "SUI/USD", price: 3.9 },
    { symbol: "LINK/USD", price: 16.02 },
    { symbol: "AVAX/USD", price: 23.05 },
    { symbol: "XLM/USD", price: 0.2932 },
    { symbol: "WBTC/USD", price: 103120.8 },
    { symbol: "DOT/USD", price: 6.5 },
    { symbol: "SHIB/USD", price: 0.000015 },
    { symbol: "LTC/USD", price: 99.51 },
    { symbol: "UNI/USD", price: 6.31 },
    { symbol: "BCH/USD", price: 408.72 },
  ];
  return (
    <Container fluid className="mt-4">
      <Row>
        <Col>
          <UserInfo user={user} />
          <Transactions />
        </Col>
        <Col>
          <Holdings prices={marketPrices} />

          <MarketData rows={marketPrices} user={user} />
        </Col>
      </Row>
    </Container>
  );
}

export default App;
