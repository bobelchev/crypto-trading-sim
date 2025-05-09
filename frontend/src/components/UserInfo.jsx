import { useState, useEffect } from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";

function UserInfo({ user }) {
  const handleReset = () => {
    alert("Reset");
  };

  return (
    <>
      <h4 className="mb-3">Account information</h4>
      <Card bg="light" className="mb-3 shadow-sm w-100">
        <div className="d-flex flex-wrap justify-content-between">
          <Card.Text>
            <strong>User ID:</strong> {user.userId}
          </Card.Text>
          <Card.Text>
            <strong>Balance:</strong> ${Number(user.balance).toLocaleString()}
          </Card.Text>
          <Button variant="outline-primary" onClick={handleReset}>
            Reset
          </Button>
        </div>
      </Card>
    </>
  );
}

export default UserInfo;
