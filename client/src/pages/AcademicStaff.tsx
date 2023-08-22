import { Sidebar } from "../features/sidebar";
import TextAndButtonWithPhotoOnRight from "../common_components/Card/TextAndButtonWithPhotoOnRight";
import TeachingAcademicStaff from "./assets/teaching-academic-staff.webp";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import { Link } from "react-router-dom";

export default function AcademicStaff() {
  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Teaching",
      redirectUrl: "/teaching",
      active: false,
    },
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
      active: true,
    },
    {
      title: "Research",
      redirectUrl: "/research",
      active: false,
    },
  ];

  return (
    <div className={"flex h-full"}>
      {/* Sidebar */}
      <Sidebar links={sidebarLinks} />
      <main className={"h-full w-full flex flex-col py-8"}>
        {/* Title and text */}
        <section className={"px-4 lg:px-8 mb-6"}>
          <h1 className="page-title_h1 text-brandMainNearlyBlack">
            Uncover the Faces Behind Aquila University's Excellence!
          </h1>

          <p className={"text-grayscaleDarkText lg:pr-20"}>
            Ever wondered who drives the dynamic learning at Aquila? Our
            exceptional teaching team is the heartbeat of our educational
            prowess. Get a glimpse into the lives of educators who are invested
            in your success. Explore their expertise, passion, and dedication to
            shaping tomorrow's leaders. Embark on a journey to discover the
            educators who will inspire your growth and fuel your ambitions.
            Start exploring now!
          </p>
        </section>
        {/*<ul*/}
        {/*    className={*/}
        {/*      "grid grid-cols-1 gap-x-12 gap-y-8 px-2 sm:px-6 lg:px-0 list-disc justify-center items-center ml-8 md:grid-cols-2"*/}
        {/*    }*/}
        {/*>*/}
        {/*  {courses.map((course) => (*/}
        {/*      <li>*/}
        {/*        <Link*/}
        {/*            to={`/courses/${course.id}`}*/}
        {/*            key={course.id.toString()}*/}
        {/*            className={*/}
        {/*              "text-xl text-brandMain font-bold hover:text-brandMainActive hover:underline"*/}
        {/*            }*/}
        {/*        >*/}
        {/*          <div>*/}
        {/*            {course.title} by the {course.faculty.name}{" "}*/}
        {/*            <ArrowUpRightBrandMain className={"inline h-6 w-6"} />*/}
        {/*          </div>*/}
        {/*        </Link>*/}
        {/*      </li>*/}
        {/*  ))}*/}
        {/*</ul>*/}
      </main>
    </div>
  );
}
