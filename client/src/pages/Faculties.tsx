import ColoredBackgroundWithPhotoOnRight from "../common_components/Card/ColoredBackgroundWithPhotoOnRight";
import FacultiesPagePhoto from "./assets/faculties-page.webp";

export default function Faculties() {
  return (
    <main className={"p-6 lg:p-10"}>
      {/*<h1 className={"page-title_h1"}>LIST OF FACULTIES</h1>*/}
      <div className={"lg:px-16"}>
        <ColoredBackgroundWithPhotoOnRight
          heading={"Explore Our Diverse Faculties at Aquila University"}
          text={
            "Welcome to Aquila University's list of faculties, where academic excellence thrives and limitless possibilities await. Discover your passion, uncover new horizons, and embark on a journey of personal and intellectual growth."
          }
          imageLink={FacultiesPagePhoto}
        />
      </div>
      <span className={"text-grayscaleDarkText flex flex-col gap-4"}></span>
    </main>
  );
}
