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
  const [marketPrices, setMarketPrices] = useState([]);
  const fetchMarketData = () =>{
      fetch("http://localhost:8080/marketData")
            .then((response) => response.json())
            .then((data) => {
              const formattedData = Object.entries(data).map(([symbol, price]) => ({
                symbol,
                price,
              }));
              formattedData.sort((a, b) => (a.price>b.price)?-1:1);
              setMarketPrices(formattedData);
            })
            .catch((err) => {
              console.error(err.message);
            });
      }
  useEffect(() => {
      const myInterval = setInterval(fetchMarketData, 1000);
      return () => {
          clearInterval(myInterval);
        };
  }, []);

  useEffect(() => {
    fetch("http://localhost:8080/users/balance?userId=1")
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setUser({
          userId: 1,
          balance: data,
        });
      })
      .catch((err) => {
        console.error(err.message);
      });
  }, []);

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
