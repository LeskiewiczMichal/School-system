import { Sidebar } from "../features/sidebar";
import { useParams } from "react-router-dom";
import { useEffect } from "react";
import { useAppSelector } from "../hooks";

export default function Article() {
  const { id } = useParams<{ id: string }>();
  // const getArticleByIdLink = useAppSelector(state => state.links.)

  useEffect(() => {
    const handleFetchArticle = async () => {};
  }, []);

  return (
    <div className={"flex h-full"}>
      <Sidebar />
      {id}
    </div>
  );
}
