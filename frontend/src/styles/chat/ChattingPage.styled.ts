import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Container = styled.div`
  position: relative;
  height: calc(100vh - 9rem);
  display: flex;
  flex-direction: column;
  border-top: 1px solid ${palette.divider};
  background-color: #f8f8f8;

  ${breakpoints.large} {
    flex-direction: row;
    height: calc(100vh - 9rem);
    padding: 0;
  }
`

export const FlexAlignCenter = styled.div`
  display: flex;
`

export const WrapChatList = styled.div`
  display: flex;
  overflow-x: auto;
  padding: 1rem 1.5rem;
  width: 100%;
  background-color: white;
  min-height: 10rem;

  ${breakpoints.large} {
    flex-direction: column;
    width: 28%;
    overflow-y: auto;
    padding: 0;
  }
`

export const RoomListButton = styled.button<{ $isActive: boolean }>`
  background-color: ${props => (props.$isActive ? '#FFF4F2' : palette.white)};
  border-radius: 1rem;

  ${breakpoints.large} {
    border-bottom: 1px solid ${palette.divider};
    border-top: 1px solid ${palette.divider};
    border-radius: 0;
  }
`

export const ReqireButton = styled.button`
  position: absolute;
  display: flex;
  align-self: flex-end;
  justify-content: center;
  padding: 0.2rem 0;
  border-radius: 0 0 1rem 1rem;
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  font-size: 1rem;
  border: 1px solid ${palette.divider};

  ${breakpoints.large} {
    width: 30%;
    font-size: ${typography.web.desc};
  }
`
export const Wrap = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
`

export const NoRoom = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, calc(-50% - 50px));
  padding: 1%;
  border-radius: 1rem;
  background-color: ${palette.divider};
  min-width: 22rem;
  text-align: center;
  ${breakpoints.large} {
    transform: translate(-50%, -50%);
  }
`
