import styled from 'styled-components'
import { zindex, palette, breakpoints } from '../../constants/Styles'

export const GreyBackground = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 100vw;
  height: 100%;
  background-color: #000000;
  opacity: 0.4;
  z-index: ${zindex.modal};
`

export const SelectMoadl = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
  height: 20%;
  background-color: ${palette.white};
  border-radius: 10px;
  text-align: center;
  z-index: ${zindex.modal + 1};
  ${breakpoints.large} {
    width: 54%;
  }
`

export const ChargingInfo = styled.div`
  width: 100%;
  height: calc(100% - 40px);
  padding: 6%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-radius: 10px 10px 0 0;
`

export const ShowCurrentInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
`

export const MainInfo = styled.p`
  font-weight: 600;
  font-size: 2rem;
`

export const MoreInfo = styled.p``

export const MoreMoney = styled.span`
  color: ${palette.main};
  font-weight: 600;
`

export const PointInfo = styled.p``

export const ButtonList = styled.div`
  display: flex;
  width: 100%;
  height: 40px;
`

export const RejectButton = styled.button`
  width: 50%;
  background-color: ${palette.divider};
  border-radius: 0 0 0 10px;
`
export const closeButton = styled.button`
  width: 100%;
  background-color: ${palette.divider};
  border-radius: 0 0 10px 10px;
`

export const AcceptButton = styled.button`
  width: 50%;
  background-color: ${palette.main};
  border-radius: 0 0 10px 0;
  color: ${palette.white};
`
