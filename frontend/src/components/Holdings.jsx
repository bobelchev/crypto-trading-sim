import Table from 'react-bootstrap/Table';

function Holdings() {
  return <Table responsive>
      <thead>
           <tr>
             <th>Crypto</th>
             <th>Quantity</th>
           </tr>
           </thead>
           <tbody>
           <tr>
             <td>BTC</td>
             <td>100.00</td>
           </tr>
           <tr>
             <td>ETH</td>
             <td>30.12</td>
           </tr>
           </tbody>
         </Table>;
}
export default Holdings;