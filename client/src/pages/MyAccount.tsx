import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";
import EnumMapper from "../utils/EnumMapper";
import { BasicInformation } from "../features/user";

export default function MyAccount() {
  const user = useAppSelector((state) => state.auth.data);
  const userLinks = useAppSelector((state) => state.auth._links);

  if (!user) {
    return (
      <div>
        <h1>Not logged in</h1>
      </div>
    );
  }

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      <main className={"pb-16 border-b border-grayscaleMediumDark w-full"}>
        <section className={"px-4 lg:px-8 py-8 mb-4 w-full sm:w-fit"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            Hello, {user.firstName}
          </h1>
          <BasicInformation user={user} />
        </section>
        <section
          className={
            "px-4 lg:px-8 lg:pr-28 py-8 mb-4 w-full border-t border-grayscaleMedium"
          }
        ></section>
      </main>
    </div>
  );
}
