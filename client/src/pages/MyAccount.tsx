import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";

export default function MyAccount() {
  const user = useAppSelector((state) => state.auth.data);

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
      <main className={"pb-16 border-b border-grayscaleMediumDark"}>
        <section className={"px-4 lg:px-8 lg:pr-28 py-8 mb-4"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            User Information
          </h1>
          <div className={"border border-brandMain"}>
            <span>Firstname: {user.firstName}</span>
            <span>Lastname: {user.lastName}</span>
            <span>Email: {user.email}</span>
          </div>
        </section>
      </main>
    </div>
  );
}
