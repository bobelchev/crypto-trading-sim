import Card from 'react-bootstrap/Card';

function UserInfo() {
  return (<Card className="p-3 mb-3">
            <Card.Text className="mb-1"><strong>User ID:</strong> user123</Card.Text>
            <Card.Text className="mb-0"><strong>Balance:</strong> $1500.50</Card.Text>
           </Card>);
}
export default UserInfo;
