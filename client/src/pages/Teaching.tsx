import { Sidebar } from "../features/sidebar";
import { SidebarLinkProps } from "../features/sidebar/components/SidebarLink";
import TextAndButtonWithPhotoOnRight from "../common_components/Card/TextAndButtonWithPhotoOnRight";
import TeachingAcademicStaff from "./assets/teaching-academic-staff.webp";

export default function Teaching() {
  const sidebarLinks: SidebarLinkProps[] = [
    {
      title: "Teaching",
      redirectUrl: "/teaching",
      active: true,
    },
    {
      title: "Academic Staff",
      redirectUrl: "/academic-staff",
      active: false,
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
            Welcome to the Aquila University Teaching Experience!
          </h1>

          <div className={"text-grayscaleDarkText lg:pr-20"}>
            <p>
              At Aquila University, our commitment to excellence in education is
              at the core of everything we do. We understand that your learning
              journey is not only about acquiring knowledge but also about
              gaining valuable skills, insights, and experiences that will shape
              your future.
            </p>

            <div className={"my-6"}>
              <TextAndButtonWithPhotoOnRight
                heading={"LEARN MORE ABOUT OUR TEACHING STAFF"}
                text={
                  "Explore the stories behind the mentors who will guide you on your educational journey. Unveil the possibilities of personalized learning and groundbreaking insights. Your future begins with the knowledge they impart. Delve into the world of Aquila's teachers today!"
                }
                buttonLink={"/academic-staff"}
                buttonText={"Meet Our Teachers"}
                imageLink={TeachingAcademicStaff}
              />
            </div>

            <h5 className={"mt-6 mb-4 text-xl font-bold italic"}>
              What Sets Our Teaching Apart?
            </h5>

            <ol className={"flex flex-col gap-3 list-decimal ml-6"}>
              <li>
                <p>
                  <strong>Engaging Learning Environment: </strong>
                  Our professors are dedicated to creating an interactive and
                  dynamic learning environment. Through innovative teaching
                  methods, real-world examples, and open discussions, we ensure
                  that your classes are both informative and exciting.
                </p>
              </li>
              <li>
                <p>
                  <strong>Expert Faculty: </strong>
                  Our teaching staff consists of accomplished experts in their
                  fields. They bring their knowledge, research insights, and
                  industry experience directly to the classroom, giving you a
                  comprehensive understanding of the subject matter.
                </p>
              </li>
              <li>
                <p>
                  <strong>Personalized Attention: </strong>
                  We believe in the power of personalized education. With
                  smaller class sizes, you'll have the opportunity to interact
                  more closely with professors, ask questions, and engage in
                  meaningful discussions that deepen your understanding.
                </p>
              </li>
              <li>
                <p>
                  <strong>Hands-On Learning: </strong>
                  Many of our programs integrate hands-on experiences, practical
                  projects, and internships. This approach allows you to apply
                  theoretical concepts to real-world scenarios, preparing you to
                  excel in your chosen career.
                </p>
              </li>
              <li>
                <p>
                  <strong>Cutting-Edge Resources: </strong>
                  Aquila University provides access to state-of-the-art
                  facilities, libraries, research centers, and technology,
                  enriching your learning experience and enabling you to explore
                  your interests to the fullest.
                </p>
              </li>
              <li>
                <p>
                  <strong>Global Perspective: </strong>
                  Our diverse student body and faculty from around the world
                  create a multicultural learning environment. Engaging with
                  different perspectives and cultures enriches your educational
                  journey and prepares you for success in a globalized world.
                </p>
              </li>
              <li>
                <p>
                  <strong>Supportive Community: </strong>
                  We value your growth, both academically and personally. Our
                  university offers various support services, including academic
                  advising, counseling, and extracurricular activities, to
                  ensure your well-rounded development.
                </p>
              </li>
            </ol>

            <h5 className={"mt-6 mb-4 text-xl font-bold italic"}>
              Why Choose Aquila University?
            </h5>
            <div className={"flex flex-col gap-3"}>
              <p>
                When you choose Aquila University, you're choosing more than
                just a degree. You're joining a community that values curiosity,
                innovation, and the pursuit of knowledge. Our teaching
                philosophy goes beyond textbooks; it's about nurturing your
                intellectual curiosity and empowering you to become a critical
                thinker and a problem solver.
              </p>
              <p>
                Whether you're embarking on your undergraduate journey or
                pursuing advanced studies, Aquila University's teaching approach
                is designed to inspire, challenge, and transform you. Your time
                here will be marked by intellectual exploration, personal
                growth, and the development of skills that will serve you
                throughout your life.
              </p>
              <p>
                We invite you to explore our diverse range of programs, engage
                with our faculty, and envision the exciting possibilities that
                await you at Aquila University. Your journey to success starts
                with the education you receive, and we're here to provide you
                with an exceptional and enriching learning experience.
              </p>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}
