import Table from 'react-bootstrap/Table';
import Button from "react-bootstrap/Button";
import { useState, useEffect } from 'react';
import SellModal from './child_components/SellModal';


function Holdings({prices}) {
     const [holdings, setHoldings] = useState([]);
     //state to open close the model
     const [show, setShow] = useState(false);
     //state to set the holding to be sold
     const [holding, setHolding] = useState(null);
     //the locked price
     const [lockedPrice, setPrice] = useState(0.0);
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
   const openModal  = (holding) => {
       setHolding(holding)
       let priceOfHolding = prices.find(p => p.symbol.includes(holding.cryptoTicker));
       console.log(priceOfHolding);
       setPrice(priceOfHolding.price);
       setShow(true);
   }
    const handleCancel = () => {
       setShow(false);
       setHolding(null);
    }
    const handleSell = async (quantityToSell, holding, lockedPrice) => {
        setShow(false);
        setHolding(null);
        if(quantityToSell>holding.quantity){
            alert("Cannot sell more than your available coins!");
        } else{

         const postBody = {
                userId: 1,
                cryptoTicker: holding.cryptoTicker,
                quantity: quantityToSell,
                price: lockedPrice,
                type: 'SELL'
         };
        try {
          const response = await fetch('http://localhost:8080/transactions', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(postBody)
          });
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }

          const jsonResponse = await response.text();
          alert("Transaction successful:",jsonResponse);
          console.log('POST request successful:', jsonResponse);
          console.log(quantityToSell);
          //for now like that
          window.location.reload();
        } catch (error) {
            alert('Transaction failed: ',error)
          console.error('POST request failed:', error);
        }

        }
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
                   <td><Button variant="outline-danger" onClick={() => openModal(holding)}>
                        Sell
                    </Button> </td>
               </tr>
             ))}
           </tbody>
         </Table>
         <SellModal show={show} onCancel={handleCancel} onSell={handleSell} holding={holding} lockedPrice={lockedPrice}/>
         </>

    );
}
export default Holdings;