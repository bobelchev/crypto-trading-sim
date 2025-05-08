import Table from 'react-bootstrap/Table';

function MarketData() {
  return <Table responsive>
      <thead>
           <tr>
             <th>Crypto</th>
             <th>Price</th>
           </tr>
           </thead>
           <tbody>
           <tr>
             <td>BTC</td>
             <td>94000.0</td>
           </tr>
           <tr>
             <td>ETH</td>
             <td>1890.0</td>
           </tr>
           </tbody>
         </Table>;
}
export default MarketData;