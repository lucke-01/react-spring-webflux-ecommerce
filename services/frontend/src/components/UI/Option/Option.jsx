
import "./Option.scss";
import Card from "../Card/Card";

const Option = ({ option, handleOnClick }) => {
  return (
    <div className="option-component">
      <Card onClick={handleOnClick}>
        <div className="card-component__text">
          <span className="card-component__text--uppercase">
            {option?.description}
          </span>
          <span className="card-component__text--bold">{option?.title}</span>
        </div>
        <i className={`bi ${option?.icon} card-component__text--icon`} />
      </Card>
    </div>
  );
};

export default Option;
