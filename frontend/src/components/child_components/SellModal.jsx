import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

function SellModal({ show, onCancel, onSell, holding }) {
    const [quantity, setQuantity] = useState(0.0);



  return (
    <Modal show={show} onHide={onCancel}>
      <Modal.Header closeButton>
        <Modal.Title>Sell Crypto Asset</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        Select the quantity of {holding?.cryptoTicker} you want to sell. You have in total {holding?.quantity}.
        <Form>
          <Form.Group className="mb-3" controlId="setQuantity">
            <Form.Label>Quantity:</Form.Label>
            <Form.Control
                          type="number"
                          placeholder="0.0"
                          value={quantity}
                          onChange={(e) => setQuantity(e.target.value)}
            />
            <Form.Text className="text-muted">
              Maximum amount: {holding?.quantity}
            </Form.Text>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="danger" onClick={() => onSell(quantity, holding.quantity)}>
          Sell
        </Button>
        <Button variant="secondary" onClick={onCancel}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default SellModal;
