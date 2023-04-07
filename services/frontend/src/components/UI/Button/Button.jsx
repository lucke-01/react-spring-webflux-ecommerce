import "./Button.scss";

const Button = ({
  text = "",
  className = "",
  handleOnClick,
  loading = false,
  ...rest
}) => {
  return (
    <button
      onClick={handleOnClick}
      className={`${className} button-component`}
      disabled={loading}
      {...rest}
    >
      {loading ? (
        <svg className="ring" viewBox="25 25 50 50" strokeWidth="5">
          <circle cx="50" cy="50" r="20" />
        </svg>
      ) : (
        text
      )}
    </button>
  );
};

export default Button;
