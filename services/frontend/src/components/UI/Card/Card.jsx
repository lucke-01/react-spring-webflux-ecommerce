import "./Card.scss";

const Card = ({ children, onClick }) => {
  return (
    <div className="card-component" onClick={onClick}>
      {children}
    </div>
  );
};

export default Card;
