import { Sidebar } from "../../features/sidebar";
import FooterLinksCreator, {
  ActiveFooterPage,
} from "../../features/footer/utils/FooterLinksCreator";
import { ReactComponent as SendButtonSVG } from "../../assets/icons/arrow/arrow-up-right-brandMain.svg";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { AppPaths } from "../../App";
import { useAppSelector } from "../../hooks";
import axios from "axios";

export default function ContactPage() {
  const navigate = useNavigate();
  const sendMessageLink = useAppSelector(
    (state) => state.links.mail.sendContactEmail,
  );
  const [subject, setSubject] = useState<string>("");
  const [message, setMessage] = useState<string>("");

  const handleSend = async () => {
    if (!sendMessageLink) {
      return;
    }

    if (subject.trim() === "" || message.trim() === "") {
      alert("Please fill all the fields");
      return;
    }

    await axios.post(sendMessageLink.href, {
      subject: subject,
      message: message,
    });

    navigate(AppPaths.HOME);
  };

  const handleChangeForm = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    e.preventDefault();

    if (e.target.name === "subject") {
      setSubject(e.target.value);
    }
    if (e.target.name === "message") {
      setMessage(e.target.value);
    }
  };

  const links = FooterLinksCreator.createFooterNavigationLinks(
    ActiveFooterPage.CONTACT,
  );

  return (
    <div className={"flex h-full"}>
      <Sidebar links={links} />
      <main className={"pb-16 w-full py-6"}>
        <h1 className="page-title_h1 px-4 lg:px-6 text-brandMainNearlyBlack">
          CONTACT US
        </h1>

        <section className={"flex flex-col gap-2 px-4 lg:px-8"}>
          <span>
            <b>Email: </b>leskiewicz02@gmail.com
          </span>
          <a
            className={" underline"}
            href="https://linkedin.com/in/michał-leśkiewicz-0b964425b"
          >
            Go to linkedin
          </a>
        </section>

        <form className={"border border-grayscaleDark px-4 py-8 mb-12 mt-8"}>
          <h6 className={"font-bold text-brandMainNearlyBlack text-xl mb-8"}>
            Give anonymous feedback
          </h6>

          {/* Subject */}
          <div className="flex flex-col mb-8">
            <label htmlFor="search-dropdown" className="mb-2 font-bold">
              Subject
            </label>

            <div className="relative w-full lg:w-1/3">
              <input
                type="text"
                id="subject"
                name="subject"
                className="block p-4 w-full font-bold text-sm text-black bg-grayscaleLight border-2 border-grayscaleMediumDark focus:outline-none focus:ring-none focus:border-brandMain"
                placeholder="What is it about?"
                onChange={handleChangeForm}
                value={subject}
                required
              />
            </div>
          </div>

          {/* Message */}
          <div className={"flex flex-col "}>
            <label htmlFor="search-dropdown" className="mb-2 font-bold">
              Message
            </label>

            {/* Message */}
            <div className="relative w-full">
              <textarea
                id="message"
                name="message"
                rows={8}
                className="block p-4 w-full text-sm text-black bg-grayscaleLight border-2 border-grayscaleMediumDark focus:outline-none focus:ring-none focus:border-brandMain"
                placeholder="What do you want to tell us?"
                onChange={handleChangeForm}
                value={message}
                required
              />
            </div>
          </div>

          <div className={"w-full flex justify-end mt-2"}>
            <button
              className={
                "flex items-center text-xl font-bold text-brandMain hover:underline hover:text-brandMainActive"
              }
              type={"button"}
              onClick={handleSend}
            >
              Send <SendButtonSVG className={"w-8 h-8"} />
            </button>
          </div>
        </form>
      </main>
    </div>
  );
}
