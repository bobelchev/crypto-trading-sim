import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";
import BuyModal from './child_components/BuyModal';
import { useState, useEffect } from 'react';


function MarketData({ rows }) {
     //state to open close the model
     const [show, setShow] = useState(false);
     //the locked price
     const [assetPrice, setPrice] = useState(0.0);
     const [row, setRow] = useState(null);

     const openModal  = (row) => {
           //all the info for the chosen crypto
           setRow(row);
           //price of the crypto asset chosen to be bough
           let priceOfRow = row.price;
           console.log(priceOfRow);
           setPrice(priceOfRow);
           setShow(true);
       }
     const handleCancel = () => {
           setShow(false);
           setPrice(0.0);
        }
    const handleBuy = () => {
        setShow(false);
        setPrice(0.0);
    }

  return <>
   <BuyModal show={show} onCancel={handleCancel} onBuy={handleBuy} row={row} lockedPrice={assetPrice}/>
   <h4 className="mb-1">Market Data</h4>
  <div style={{ overflowY: 'scroll', height: '400px'}}>
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
                             <td><Button variant="outline-success" onClick={() => openModal(row)}>
                                 Buy
                             </Button> </td>
                           </tr>
                         ))}
           </tbody>
         </Table>
         </div>

         </>
}
export default MarketData;