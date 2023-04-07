import "./Loader.scss";

const Loader = () => {
  return (
    <div className="loader-component">
      <svg className="ring" viewBox="25 25 50 50" strokeWidth="5">
        <circle cx="50" cy="50" r="20" />
      </svg>
    </div>
  );
};

export default Loader;
