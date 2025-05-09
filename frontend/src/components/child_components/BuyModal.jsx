import { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';

function BuyModal({show, onCancel, onSell, row, lockedPrice}) {
    const [quantity, setQuantity] = useState('');

  return (
    <Modal show={show} onHide={onCancel}>
      <Modal.Header closeButton>
        <Modal.Title>Sell Crypto Asset</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        Select the quantity of {row?.cryptoTicker} you want to buy. You have in total balance.
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
              Maximum amount according to balance:
            </Form.Text>
            <p>Total: ${lockedPrice* parseFloat(quantity)}</p>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="success" onClick={() => onBuy()}>
          Sell
        </Button>
        <Button variant="secondary" onClick={onCancel}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default BuyModal;
