import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import UserInfo from './components/UserInfo';
import Holdings from './components/Holdings';
import "bootstrap/dist/css/bootstrap.css";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";



function App() {
  const [count, setCount] = useState(0)

  return (
          <Container fluid className="mt-4">
            <Row>
              <Col>
                <UserInfo />
              </Col>
              <Col>
                <Holdings />
              </Col>
            </Row>
          </Container>
        );

}

export default App
