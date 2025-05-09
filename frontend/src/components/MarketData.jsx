import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import BuyModal from "./child_components/BuyModal";
import { useState, useEffect } from "react";

function MarketData({ rows, user }) {
  //state to open close the model
  const [show, setShow] = useState(false);
  //the locked price
  const [assetPrice, setPrice] = useState(0.0);
  //the price and the crypto to be bought
  const [row, setRow] = useState(null);

  const openModal = (row) => {
    //all the info for the chosen crypto
    setRow(row);
    //price of the crypto asset chosen to be bough
    let priceOfRow = row.price;
    console.log(priceOfRow);
    setPrice(priceOfRow);
    setShow(true);
  };
  const handleCancel = () => {
    setShow(false);
    setPrice(0.0);
  };
  const handleBuy = async(quantityToBuy, crypto, lockedPrice,balance) => {
    const pair = crypto;
    const baseCurrency = pair.split("/")[0];
    console.log(quantityToBuy);
    console.log(baseCurrency);
    console.log(lockedPrice);
    if (quantityToBuy*lockedPrice > balance) {
          alert("Cannot buy more than your available balance!");
        } else {
          const postBody = {
            userId: user.userId,
            cryptoTicker: baseCurrency,
            quantity: quantityToBuy,
            price: lockedPrice,
            type: "BUY",
          };
          try {
            const response = await fetch("http://localhost:8080/transactions", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(postBody),
            });
            if (!response.ok) {
              throw new Error("Network response was not ok");
            }
            const jsonResponse = await response.text();
            alert(`Transaction successful: ${jsonResponse}`);
            console.log("POST request successful:", jsonResponse);
            console.log(quantityToBuy);
            //for now like that
            window.location.reload();
          } catch (error) {
            alert(`Transaction failed: ${error.message}`);
            console.error("POST request failed:", error);
          }
        }
    setShow(false);
    setPrice(0.0);
  };

  return (
    <>
      <BuyModal
        show={show}
        onCancel={handleCancel}
        onBuy={handleBuy}
        row={row}
        lockedPrice={assetPrice}
        user={user}
      />
      <h4 className="mb-1">Market Data</h4>
      <div style={{ overflowY: "scroll", height: "400px" }}>
        <Table
          striped
          bordered
          hover
          responsive="md"
          className="rounded shadow-sm"
        >
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
                <td>
                  <Button
                    variant="outline-success"
                    onClick={() => openModal(row)}
                  >
                    Buy
                  </Button>{" "}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </>
  );
}
export default MarketData;
