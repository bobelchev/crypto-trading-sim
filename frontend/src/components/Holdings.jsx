import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";

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
           {rows.map((row) => (
               <tr key={row.symbol}>
                   <td>{row.symbol}</td>
                   <td>{row.quantity}</td>
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