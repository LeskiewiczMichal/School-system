import LoadingSpinner from "../common_components/LoadingSpinner";
import { useAppSelector } from "../hooks";
import { Sidebar } from "../features/sidebar";

export default function LoadingSpinnerPage() {
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );

  return (
    <div
      className={"h-screen w-screen flex flex-col justify-center items-center"}
    >
      {mobileNavBar && <Sidebar />}
      <LoadingSpinner />
    </div>
  );
}
