import "./Confirmation.scss";
import Card from "../Card/Card";
import Button from "../Button/Button";

const Confirmation = ({ visibility, handleOnDelete, handleOnClose, deleteConfirmation }) => {
  const confirmationMessage = deleteConfirmation || true ? 
      "Are you sure you want to delete this resource ?\n This action can not be undone." :
      "Are you sure you want to do this?";
  return (
    <>
      <div
        className={
          visibility
            ? "confirmation-component active"
            : "confirmation-component"
        }
      >
        <Card>
          <div style={{"whiteSpace": 'pre-wrap'}}>
            {confirmationMessage}
          </div>
          <div className="confirmation-component__buttons d-flex justify-content-center">
            <Button className="button-accept" text="Delete" handleOnClick={handleOnDelete} />
            <Button className="button-cancel" text="Cancel" handleOnClick={handleOnClose} />
          </div>
        </Card>
      </div>
      <div
        className={
          visibility
            ? "confirmation-component__bg active"
            : "confirmation-component__bg"
        }
      ></div>
    </>
  );
};

export default Confirmation;
