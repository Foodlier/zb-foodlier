import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
  zindex,
} from '../../constants/Styles'

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

export const ModalContainer = styled.div`
  position: fixed;
  display: flex;
  flex-direction: column;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%;
  height: 80%;
  max-width: 1000px;
  background-color: ${palette.white};
  padding: 5%;
  border-radius: 10px;
  z-index: ${zindex.modal};
  overflow-y: auto;
  ${breakpoints.large} {
    padding: 3%;
    width: 70%;
  }
`

export const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
`

export const ModalTitle = styled.span`
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const CloseButton = styled.button``

export const ElContainer = styled.ul`
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 1.5rem;
  padding-left: 0;
`

export const El = styled.li`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  border-radius: 5px;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
  ${breakpoints.large} {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }
`

export const ElImg = styled.img`
  width: 70px;
  min-width: 70px;
  height: 70px;
  background-color: yellowgreen;
`

export const ElContents = styled.p`
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`
export const ButtonContainer = styled.div`
  display: flex;
  gap: 5px;
  button {
    height: 30px;
  }
  ${breakpoints.large} {
    flex-direction: column-reverse;
    align-items: end;
  }
`

export const RequestButton = styled.button`
  width: 80%;
  color: ${palette.white};
  background-color: ${palette.main};
  border-radius: 5px;
  ${breakpoints.large} {
    width: 120px;
  }
`

export const MoreButtonContainer = styled.div`
  width: 20%;
  display: flex;
  gap: 1px;
  ${breakpoints.large} {
    width: 120px;
  }
`

export const MoreButton = styled.button`
  width: 50%;
  background-color: ${palette.divider};
  border-radius: 5px 0 0 5px;
  &:last-of-type {
    border-radius: 0 5px 5px 0;
  }
  ${breakpoints.large} {
    width: 100%;
  }
`

export const EmptyView = styled.div`
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
  color: ${palette.textDisablePlace};
`

export const AddListButton = styled.button`
  align-self: flex-end;
`
