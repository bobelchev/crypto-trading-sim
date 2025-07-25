import { useState, useEffect, useRef } from "react";
import "../App.css";
import UserInfo from "./UserInfo";
import Holdings from "./Holdings";
import MarketData from "./MarketData";
import Transactions from "./Transactions";

import "bootstrap/dist/css/bootstrap.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

function App() {
  const [user, setUser] = useState({ userId: "", balance: "" });
  const [marketPrices, setMarketPrices] = useState([]);
  const connection = useRef(null);
  useEffect(() => {
    const token = sessionStorage.getItem("jwt");
    const socket = new WebSocket(
      "ws://localhost:8080/ws/marketdata",
    );

    socket.addEventListener("open", (event) => {
      socket.send("Connection established");
    });

    socket.addEventListener("message", (event) => {
      const data = JSON.parse(event.data);
      const formattedData = Object.entries(data).map(([symbol, price]) => ({
        symbol,
        price,
      }));
      formattedData.sort((a, b) => (a.price > b.price ? -1 : 1));
      setMarketPrices(formattedData);
      console.log("Message from server ", data);
    });

    connection.current = socket;

    return () => connection.current.close();
  }, []);

  /*const fetchMarketData = () =>{
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
      const myInterval = setInterval(fetchMarketData, 500);
      return () => {
          clearInterval(myInterval);
        };
  }, []);*/

  useEffect(() => {
    const token = sessionStorage.getItem("jwt");
    if (!token) return;
    fetch("http://localhost:8080/users/balance?userId=1", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
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
