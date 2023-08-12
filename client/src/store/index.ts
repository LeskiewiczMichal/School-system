import { RootState, AppDispatch, AppThunk } from "./types";
import store from "./store";
import IntegrationSlice from "./slice/IntegrationSlice";
import {
  setSidebarMenuActive,
  setMobileNavView,
} from "./slice/IntegrationSlice";

const IntegrationSliceActions = {
  setSidebarMenuActive,
  setMobileNavView,
};

export { store, IntegrationSlice, IntegrationSliceActions };
export type { RootState, AppDispatch, AppThunk };
