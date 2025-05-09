import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";
import { useState, useEffect } from 'react';
import SellModal from './child_components/SellModal';


function Holdings({prices}) {
     const [holdings, setHoldings] = useState([]);
     const [show, setShow] = useState(false);


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
   const openModal  = () => {
       setShow(true);
   }
    const handleClose = () => {
       setShow(false);
       }

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
                   <td><Button variant="outline-danger" onClick={openModal}>
                        Sell
                    </Button> </td>
               </tr>
             ))}
           </tbody>
         </Table>;

         <SellModal show={show} onHide={handleClose} holding="Btc"/>
         </>

    );
}
export default Holdings;