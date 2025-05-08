import Card from 'react-bootstrap/Card';

function Transactions() {
  return (
      <>
      <h4 className="mb-3">Transaction history</h4>

    <Card border="success" className="mb-3 shadow-sm w-100">
      <Card.Body>
        <Card.Title>BUY-BTC</Card.Title>
            <div className="d-flex flex-wrap justify-content-between">
          <div><strong>Quantity:</strong>0.5</div>
          <div><strong>Total Price:</strong>$15000.00</div>
          <div><strong>Date:</strong>2024-05-08 12:34:56</div>
          </div>
      </Card.Body>
    </Card>
    </>
  );
}

export default Transactions;
