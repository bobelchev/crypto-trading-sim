import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import UserInfo from './components/UserInfo';
import Holdings from './components/Holdings';
import MarketData from './components/MarketData';
import Transactions from './components/Transactions';


import "bootstrap/dist/css/bootstrap.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";



function App() {
    const marketPrices = [
        {symbol: "BTC/USD", price: 94000.0 },
        {symbol: "ETH/USD", price: 1800.0 },
        {symbol: "DOGE/USD", price: 1.0 },
        {symbol: "XRP/USD", price: 2.0 },
        {symbol: "BNB/USD", price: 600.0 },
        {symbol: "TRON/USD", price: 13.0 },
        {symbol: "USDT/USD", price: 1.005 }
      ];
  return (
          <Container fluid className="mt-4">
            <Row>
              <Col>
                <UserInfo />
                <Transactions />
              </Col>
              <Col >
                <Holdings prices={marketPrices} />
              </Col>
            </Row>
          <Row>
              <Col >
                  <MarketData rows={marketPrices} />
              </Col>
          </Row>
          </Container>
        );

}

export default App
