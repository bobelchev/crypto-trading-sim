import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";


function MarketData() {
    // Sample data
    //example from https://www.geeksforgeeks.org/how-to-add-vertical-scrollbar-to-react-bootstrap-table-body/
      const rows = [
        {symbol: "BTC/USD", price: 94000.0 },
        {symbol: "ETH/USD", price: 1800.0 },
        {symbol: "DOGE/USD", price: 1.0 },
        {symbol: "XRP/USD", price: 2.0 },
        {symbol: "BNB/USD", price: 600.0 },
        {symbol: "TRON/USD", price: 13.0 },
        {symbol: "USDT/USD", price: 1.005 }
      ];

  return <>
        <h4 className="mb-3">Market Data</h4>
  <Table striped bordered hover responsive="md"  className="rounded shadow-sm">
      <thead>
           <tr>
             <th>Crypto</th>
             <th>Price</th>
             <th></th>
           </tr>
           </thead>
           <tbody>
               {rows.map((row) => (
                           <tr key={row.symbol}>
                             <td>{row.symbol}</td>
                             <td>{row.price}</td>
                             <td><Button variant="outline-success">
                                 Buy
                             </Button> </td>
                           </tr>
                         ))}
           </tbody>
         </Table>
         </>
}
export default MarketData;