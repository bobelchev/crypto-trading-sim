import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";


function MarketData({ rows }) {
    // Sample data
    //example from https://www.geeksforgeeks.org/how-to-add-vertical-scrollbar-to-react-bootstrap-table-body/


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