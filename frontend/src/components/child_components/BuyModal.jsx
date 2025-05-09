import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";

function BuyModal({ show, onCancel, onBuy, row, lockedPrice, user }) {
  const [quantity, setQuantity] = useState("");

  return (
    <Modal show={show} onHide={onCancel}>
      <Modal.Header closeButton>
        <Modal.Title>Buy Crypto Asset</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        Select the quantity of {row?.symbol} you want to buy. You have in total
        balance.
        <Form>
          <Form.Group className="mb-3" controlId="setQuantity">
            <Form.Label>Quantity:</Form.Label>
            <Form.Control
              type="number"
              min="0.0"
              placeholder="0.0"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
            />
            <Form.Text className="text-muted">
              Maximum amount according to available balance:{" "}
              {(user.balance / lockedPrice).toFixed(6)}
            </Form.Text>
            <p>Total: ${lockedPrice * parseFloat(quantity)}</p>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="success"
          onClick={() => onBuy(quantity, row?.symbol, lockedPrice)}
        >
          Buy
        </Button>
        <Button variant="secondary" onClick={onCancel}>
          Cancel
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default BuyModal;
