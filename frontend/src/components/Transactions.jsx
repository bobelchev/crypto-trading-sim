import Card from 'react-bootstrap/Card';
import { useState, useEffect } from 'react';

function Transactions() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
             fetch('http://localhost:8080/transactions?userId=1')
               .then((response) => response.json())
               .then((data) => {
                 console.log(data);
                 data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
                 setTransactions(data);
               })
               .catch((err) => {
                 console.error(err.message);
               });
           }, []);
  return (
      <>
      <h4 className="mb-3">Transaction history</h4>
       <div style={{ overflowY: 'scroll', height: '550px'}}>
    {transactions.map((transaction) => (
    <Card border={transaction.transactionType === 'BUY' ? 'success' : 'danger'} className="mb-3 shadow-sm w-100">
      <Card.Body>
        <Card.Title>{transaction.transactionType}-{transaction.cryptoTicker}</Card.Title>
            <div className="d-flex flex-wrap justify-content-between">
                <div><strong>Quantity:</strong> {transaction.quantity}</div>
                <div><strong>Total Price:</strong> {transaction.price}</div>
                 <div><strong>Date:</strong> {new Date(transaction.timestamp).toLocaleString()}</div>
          </div>
      </Card.Body>
    </Card>
    ))}
    </div>
    </>
  );
}

export default Transactions;
