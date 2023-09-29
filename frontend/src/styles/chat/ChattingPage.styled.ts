import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  border-top: 1px solid ${palette.divider};
  padding-bottom: 10rem;

  ${breakpoints.large} {
    flex-direction: row;
    height: calc(100vh - 9rem);
    padding-bottom: 0;
  }
`

export const FlexAlignCenter = styled.div`
  display: flex;
`

export const WrapChatList = styled.div`
  display: flex;
  overflow-x: auto;
  padding: 1.5rem;
  ${breakpoints.large} {
    flex-direction: column;
    width: 35%;
    overflow-y: auto;
    padding: 0;
  }
`

export const ChattingRoom = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  border-left: 1px solid ${palette.divider};
`

export const ChattingHeader = styled.div`
  display: flex;
  align-items: center;
  background-color: #dbdbdb;
  padding: 1.4rem 2rem;

  ${breakpoints.large} {
    padding: 2rem 5rem;
  }
`

export const ProfileImage = styled.img<{ $size: number }>`
  width: ${props => props.$size}rem;
  height: ${props => props.$size}rem;
  border-radius: 5rem;
  margin-right: 1rem;
  border: 1px solid black;
`

export const Nickname = styled.span`
  font-size: 1.4rem;

  ${breakpoints.large} {
    font-size: 1.8rem;
  }
`

export const ChattingMessage = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column-reverse;
  align-items: center;
  /* justify-content: flex-end; */
  background-color: #f8f8f8;
  padding: 1rem 3rem;
  /* max-height: 50rem; */
  overflow-y: auto;
`

export const WrapMessage = styled.div<{ $isMe: boolean }>`
  align-self: ${props => (props.$isMe ? 'flex-end' : 'flex-start')};
  display: flex;
  flex-direction: ${props => (props.$isMe ? 'row-reverse' : 'row')};
  align-items: flex-end;
  margin-bottom: 1rem;
`

export const Message = styled.div<{ $isMe: boolean }>`
  max-width: 25rem;
  background-color: ${props => (props.$isMe ? palette.main : palette.white)};
  color: ${props => (props.$isMe ? palette.white : palette.textPrimary)};
  padding: 1rem;
  border-radius: 1rem;
  font-size: 1.2rem;

  ${breakpoints.large} {
    max-width: 40rem;
    font-size: 1.4rem;
  }
`

export const MessageTime = styled.span`
  font-size: 1rem;
  margin: 0 0.5rem;

  color: ${palette.textSecondary};

  ${breakpoints.large} {
    font-size: 1.2rem;
    margin: 0 1rem;
  }
`

export const WrapInput = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 1rem 1.5rem;

  ${breakpoints.large} {
    padding: 2rem 5rem;
  }
`

export const Input = styled.input`
  border: 1px solid ${palette.divider};
  padding: 0.8rem;
  border-radius: 1rem;
  width: 68%;
  font-size: 1.2rem;

  ${breakpoints.large} {
    font-size: 1.4rem;
    padding: 1rem;
    width: 80%;
  }
`

export const Button = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 1rem;
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;

  ${breakpoints.large} {
    padding: 1rem 2rem;
    font-size: 1.4rem;
  }
`

export const WrapDate = styled.div`
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  border: 1px solid ${palette.divider};
  padding: 0.5rem 2rem;
  border-radius: 1.5rem;
  font-size: 1.2rem;
`
