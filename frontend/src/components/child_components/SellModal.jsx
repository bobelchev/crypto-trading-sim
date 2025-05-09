import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

// Modal implementation reference:
// https://react-bootstrap.netlify.app/docs/components/modal/
function SellModal({ show, onHide, holding }) {

  return (<>
      <Modal show={show} onHide={onHide}>
              <Modal.Header closeButton>
                <Modal.Title>Sell Crypto Asset</Modal.Title>
              </Modal.Header>
              <Modal.Body>Select the quantity of {holding} you want to sell</Modal.Body>
              <Modal.Footer>
                <Button variant="danger" onClick={onHide}>
                  Sell
                </Button>
                <Button variant="secondary" onClick={onHide}>
                  Cancel
                </Button>
              </Modal.Footer>
            </Modal>
      </>

  );
}

export default SellModal;