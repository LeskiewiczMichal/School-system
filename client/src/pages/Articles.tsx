import { Sidebar } from "../sidebar";

export default function Articles() {
  return (
    <div className={"flex"}>
      <Sidebar />
      <main className={" grow"}></main>
    </div>
  );
}
