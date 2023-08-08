import { RootState, AppDispatch, AppThunk } from "./types";
import store from "./store";
import IntegrationSlice from "./slice/IntegrationSlice";
import { setMobileNavBarIconActive } from "./slice/IntegrationSlice";

const IntegrationSliceActions = {
  setMobileNavBarIconActive,
};

export { store, IntegrationSlice, IntegrationSliceActions };
export type { RootState, AppDispatch, AppThunk };
