import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";
import { useState, useEffect } from 'react';


function Holdings() {
     const rows = [
            {symbol: "BTC/USD", quantity: 0.5433 },
            {symbol: "ETH/USD", quantity: 2.1 },
            {symbol: "DOGE/USD", quantity: 1.0 },
            {symbol: "XRP/USD", quantity: 2.0 },
            {symbol: "BNB/USD", quantity: 600.0 },
            {symbol: "TRON/USD", quantity: 13.0 },
            {symbol: "USDT/USD", quantity: 1.005 }
          ];

     const [holdings, setHoldings] = useState([]);

       useEffect(() => {
         fetch('http://localhost:8080/holdings?userId=1')
           .then((response) => response.json())
           .then((data) => {
             console.log(data);
             setHoldings(data);
           })
           .catch((err) => {
             console.error(err.message);
           });
       }, []);

  return(
      <>
      <h4 className="mb-3">Your holdings</h4>
      <Table striped bordered hover responsive="md"  className="rounded shadow-sm">
      <thead>
           <tr>
             <th>Crypto</th>
             <th>Quantity</th>
             <th></th>
           </tr>
           </thead>
           <tbody>
           {holdings.map((holding) => (
               <tr key={holding.cryptoTicker}>
                   <td>{holding.cryptoTicker}</td>
                   <td>{holding.quantity}</td>
                   <td><Button variant="outline-danger">
                        Sell
                    </Button> </td>
               </tr>
             ))}
           </tbody>
         </Table>;
         </>
         );
}
export default Holdings;