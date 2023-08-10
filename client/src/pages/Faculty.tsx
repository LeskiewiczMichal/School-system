import Greetings from "../common_components/Card/Greetings";
import GlassBuilding from "../features/main-page/assets/glass-building.webp";

export default function Faculty() {
  return (
    <main>
      <Greetings
        imageLink={GlassBuilding}
        heading={"FACULTY OF SCIENCE"}
        text={
          "The Faculty of Science carries out top-notch research on an international level. The instruction is developed and taught by leading scientists."
        }
        buttonText={"Read more about our research!"}
      />
    </main>
  );
}
