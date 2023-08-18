interface Mapper<T> {
  mapFromServerData(data: any): T;
  mapArrayFromServerData(data: any[]): T[];
}

export default Mapper;
