import styled from 'styled-components'
import { breakpoints, palette, zindex } from '../../constants/Styles'

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

export const ModalScreen = styled.div`
  position: fixed;
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
  ${breakpoints.large} {
    padding: 3%;
    width: 70%;
  }
`

export const ModalTop = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  span,
  button {
    font-size: 2rem;
    font-weight: bold;
  }
`
export const ElContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-left: 0;
`

export const El = styled.li`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 20px;
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
export const ElTitle = styled.p`
  font-size: 1.8rem;
  font-weight: bold;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
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
