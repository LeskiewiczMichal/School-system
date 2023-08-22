import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";
import EnumMapper from "../utils/EnumMapper";
import { BasicInformation } from "../features/user";
import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import CommunityPicture from "./assets/community.webp";

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
        <section
          className={"flex flex-col px-4 lg:px-8 py-8 mb-4 w-full lg:gap-4"}
        >
          <ColoredBackgroundWithPhotoOnRight
            heading={`Hello, ${user.firstName}`}
            text={"Thank you for being a part of our community"}
            imageLink={CommunityPicture}
            backgroundColor={"brandMain"}
          />
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
