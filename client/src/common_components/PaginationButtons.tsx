import PaginationInfo from "../type/PaginationInfo";
import { ReactComponent as ChevronLeft } from "../assets/icons/chevron/chevron-left.svg";
import { ReactComponent as ChevronRight } from "../assets/icons/chevron/chevron-right.svg";

export interface PaginationButtonsProps {
  paginationInfo: PaginationInfo;
  page: number;
  changePage: (direction: "next" | "previous") => void;
}

export default function PaginationButtons(props: PaginationButtonsProps) {
  const { page, changePage, paginationInfo } = props;

  return (
    <div>
      {/*// TODO: Show the page number*/}
      {/*<span className={"flex w-full items-center justify-end pt-6"}>*/}
      {/*  Page {page + 1} of {paginationInfo.totalPages}*/}
      {/*</span>*/}
      <div className={"flex w-full items-center justify-end pt-6 gap-8"}>
        {page > 0 && (
          <button
            type={"button"}
            onClick={() => changePage("previous")}
            className={
              "flex items-center font-bold w-32 justify-center border-brandMainNearlyBlack text-brandMainNearlyBlack pr-2 py-2"
            }
          >
            <ChevronLeft className={"h-8 w-8"} /> Previous
          </button>
        )}
        {paginationInfo.totalPages > page + 1 && (
          <button
            type={"button"}
            onClick={() => changePage("next")}
            className={
              "flex items-center font-bold w-32 justify-center border-brandMainNearlyBlack text-brandMainNearlyBlack pl-2 py-2"
            }
          >
            Next <ChevronRight className={"h-8 w-8"} />
          </button>
        )}
      </div>
    </div>
  );
}
