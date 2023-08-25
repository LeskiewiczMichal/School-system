import LoadingSpinner from "../common_components/LoadingSpinner";

export default function LoadingSpinnerPage() {
  return (
    <div
      className={"h-screen w-screen flex flex-col justify-center items-center"}
    >
      <LoadingSpinner />
    </div>
  );
}
