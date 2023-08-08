import { RootState, AppDispatch, AppThunk } from "./types";
import store from "./store";
import IntegrationSlice from "./slice/IntegrationSlice";
import { setSidebarMenuActive } from "./slice/IntegrationSlice";

const IntegrationSliceActions = {
  setSidebarMenuActive,
};

export { store, IntegrationSlice, IntegrationSliceActions };
export type { RootState, AppDispatch, AppThunk };
