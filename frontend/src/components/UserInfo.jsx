import Card from 'react-bootstrap/Card';

function UserInfo() {
  return (<Card className="px-0 py-0">
      <div className="d-flex flex-wrap justify-content-center">
            <Card.Text className="px-0 py-0"><strong>User ID:</strong> user123</Card.Text>
            <Card.Text className="px-0 py-0"><strong>Balance:</strong> $1500.50</Card.Text>
      </div>
           </Card>);
}
export default UserInfo;
