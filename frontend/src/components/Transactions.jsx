import Card from 'react-bootstrap/Card';
import { useState, useEffect } from 'react';

function Transactions() {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
             fetch('http://localhost:8080/transactions?userId=1')
               .then((response) => response.json())
               .then((data) => {
                 console.log(data);
                 setTransactions(data);
               })
               .catch((err) => {
                 console.error(err.message);
               });
           }, []);
  return (
      <>
      <h4 className="mb-3">Transaction history</h4>
    {transactions.map((transaction) => (
    <Card border={transaction.transactionType === 'BUY' ? 'success' : 'danger'} className="mb-3 shadow-sm w-100">
      <Card.Body>
        <Card.Title>{transaction.transactionType}-{transaction.cryptoTicker}</Card.Title>
            <div className="d-flex flex-wrap justify-content-between">
                <div><strong>Quantity:</strong> {transaction.quantity}</div>
                <div><strong>Total Price:</strong> {transaction.price}</div>
                 <div><strong>Date:</strong> {transaction.timestamp}</div>
          </div>
      </Card.Body>
    </Card>
    ))}
    </>
  );
}

export default Transactions;
